
package com.schinizer.hackernews.business

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.schinizer.hackernews.data.HackerNewsRepository
import com.schinizer.hackernews.data.local.ItemState
import com.schinizer.hackernews.data.remote.Item
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class HackerNewsViewModelTest {

    @get:Rule
    val rule = CoroutinesTestRule()

    @RelaxedMockK
    lateinit var repo: HackerNewsRepository

    lateinit var viewmodel: HackerNewsViewModel

    private val random = Random(0)

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewmodel = HackerNewsViewModel(repo, io = rule.testDispatcher)
    }

    @Test
    fun `refreshData clears job pool and updates dataFlow`() = runTest {
        val topStories = List(500) { random.nextInt() }
        val updatedStories = List(500) { random.nextInt() }
        val existingPool = mutableMapOf<Int, Job>(
            random.nextInt() to mockk(relaxed = true),
            random.nextInt() to mockk(relaxed = true),
            random.nextInt() to mockk(relaxed = true)
        )

        with(viewmodel) {
            jobPool.putAll(existingPool)
            itemOrder.addAll(topStories)
        }

        coEvery { repo.top500Stories() } returns updatedStories

        viewmodel.refreshData()

        verify(exactly = 1) {
            existingPool.forEach { (_, job) -> job.cancel() }
        }

        assertThat(viewmodel.jobPool).isEmpty()
        assertThat(viewmodel.itemOrder).isEqualTo(updatedStories)
        coVerify(exactly = 1) { repo.top500Stories() }

        viewmodel.isLoadingFlow.test {
            assertThat(expectMostRecentItem()).isFalse()
        }
        viewmodel.dataFlow.test {
            val expected = viewmodel.itemOrder.map { ItemState(id = it) }
            val actual = expectMostRecentItem().map { ItemState(id = it.id) }
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `loadItem() success case`() = runTest {
        val id = random.nextInt()
        val topStories = listOf(id)
        val item = mockk<Item>(relaxed = true)

        coEvery { repo.fetchItem(id) } returns item

        with(viewmodel) {
            itemOrder.addAll(topStories)
        }

        viewmodel.loadItem(id)

        coVerify(exactly = 1) { repo.fetchItem(id) }
        assertThat(viewmodel.data[id]).isEqualTo(item)

        viewmodel.dataFlow.test {
            val expected = listOf(ItemState(id = id))
            val actual = expectMostRecentItem().map { ItemState(id = it.id) }
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `cancelLoadItem() success case`() = runTest {
        val id = random.nextInt()
        val job = mockk<Job>(relaxed = true)

        with(viewmodel) {
            jobPool[id] = job
        }

        viewmodel.cancelLoadItem(id)

        coVerify(exactly = 1) { job.cancel() }
        assertThat(viewmodel.jobPool).isEmpty()
    }

    @Test
    fun `openItem() will emit OpenBrowser() if item is a story`() = runTest {
        val id = random.nextInt()
        val url = "http://somewebsite.com"
        val item = mockk<Item.Story>(relaxed = true)

        every { item.url } returns url

        with(viewmodel) {
            data.put(id, item)
        }

        viewmodel.actionFlow.test {
            viewmodel.openItem(id)
            val expected = HackerNewsViewModel.OpenBrowser(url)
            assertThat(expectMostRecentItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `openItem() will emit ShowUnsupportedSnackBar if item is not supported`() = runTest {
        val id = random.nextInt()
        val item = mockk<Item>(relaxed = true)

        with(viewmodel) {
            data.put(id, item)
        }

        viewmodel.actionFlow.test {
            viewmodel.openItem(id)
            val expected = HackerNewsViewModel.ShowUnsupportedSnackBar
            assertThat(expectMostRecentItem()).isEqualTo(expected)
        }
    }
}