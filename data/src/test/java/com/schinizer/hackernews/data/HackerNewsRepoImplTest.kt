package com.schinizer.hackernews.data

import com.google.common.truth.Truth.assertThat
import com.schinizer.hackernews.data.remote.Item
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class HackerNewsRepoImplTest {

    @RelaxedMockK
    lateinit var local: HackerNewsLocalSource

    @RelaxedMockK
    lateinit var remote: HackerNewsRemoteSource

    lateinit var repo: HackerNewsRepoImpl

    private val random = Random(0)

    @Before
    fun init() {
        MockKAnnotations.init(this)
        repo = HackerNewsRepoImpl(local = local, remote = remote)
    }

    @Test
    fun `top500Stories() success from remote`() = runTest {
        val ids = List(500) { random.nextInt() }
        coEvery { remote.top500Stories() } returns ids

        val result = repo.top500Stories()

        coVerify(exactly = 1) {
            remote.top500Stories()
            local.saveTop500Stories(ids)
        }
        assertThat(result).isEqualTo(ids)
    }

    @Test
    fun `top500Stories() failed remote returns local`() = runTest {
        val ids = List(500) { random.nextInt() }
        coEvery { remote.top500Stories() } returns emptyList()
        coEvery { local.top500Stories() } returns ids

        val result = repo.top500Stories()

        coVerify(exactly = 1) {
            remote.top500Stories()
            local.top500Stories()
        }
        assertThat(result).isEqualTo(ids)
    }

    @Test
    fun `fetchItem success from remote`() = runTest {
        val id = random.nextInt()
        val item = mockk<Item>(relaxed = true)
        coEvery { remote.fetchItem(id) } returns item

        val result = repo.fetchItem(id)

        coVerify(exactly = 1) {
            remote.fetchItem(id)
            local.saveItem(item)
        }
        assertThat(result).isEqualTo(item)
    }

    @Test
    fun `fetchItem failed remote will return local`() = runTest {
        val id = random.nextInt()
        val item = mockk<Item>(relaxed = true)
        val exception = RuntimeException("heh")
        coEvery { remote.fetchItem(id) } throws exception
        coEvery { local.fetchItem(id) } returns item

        val result = repo.fetchItem(id)

        coVerify(exactly = 1) {
            remote.fetchItem(id)
            local.fetchItem(id)
        }
        assertThat(result).isEqualTo(item)
    }
}