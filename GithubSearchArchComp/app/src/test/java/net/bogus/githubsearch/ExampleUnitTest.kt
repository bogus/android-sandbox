package net.bogus.githubsearch

import android.databinding.ObservableField
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.stub
import net.bogus.githubsearch.viewmodel.MainActivityViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    var viewModel = MainActivityViewModel()
    @Before
    fun runBefore() {
        viewModel.isLoading.set(false)
        viewModel.currentPage.set(0)
    }

    @Test
    fun load_nextPage() {
        var result = viewModel.shouldLoadNextPage(10, 20, 18);
        assertTrue(result)
    }

    @Test
    fun load_nextPage_false() {
        var result = viewModel.shouldLoadNextPage(10, 20, 9);
        assertFalse(result)
    }
}
