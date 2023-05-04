import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.yourstory.model.StoryRequest
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.viewmodel.story.StoryViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: StoryViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope =
        createTestCoroutineScope(TestCoroutineDispatcher() + TestCoroutineExceptionHandler() + testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StoryViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test successful data loading`() {

        val expectedList = listOf(
            StoryResponseData("1", "Title 1", "Body 1", "url1", "2023-05-03", 0.0, 0.0),
            StoryResponseData("2", "Title 2", "Body 2", "url2", "2023-05-03", 0.0, 0.0)
        )
        val response = Response.success(StoryRequest(false, "Success", expectedList))
        val token = "testToken"

        runBlockingTest {
            doReturn(response).`when`(repository).getAllStories(token, null, null, null)
            viewModel.GETStoriesList(token)
            delay(1000) // wait for the data to be loaded

            assertNotNull(viewModel._storiesList.value)
            assertEquals(expectedList.size, viewModel._storiesList.value?.size)
            assertEquals(expectedList[0], viewModel._storiesList.value?.get(0))
            assertEquals(expectedList[1], viewModel._storiesList.value?.get(1))
        }
    }

    @Test
    fun `test no data available`() {
        val response = Response.success(StoryRequest(false, "No data", emptyList()))
        val token = "testToken"

        runBlockingTest {
            doReturn(response).`when`(repository).getAllStories(token, null, null, null)
            viewModel.GETStoriesList(token)
            delay(1000) // wait for the data to be loaded

            assertNotNull(viewModel._storiesList.value)
            assertEquals(0, viewModel._storiesList.value?.size)
        }
    }

}
