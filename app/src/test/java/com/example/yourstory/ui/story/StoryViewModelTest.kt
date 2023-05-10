import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.yourstory.model.StoryRequest
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.ui.utils.DataDummy
import com.example.yourstory.view.story.recyclerview.adapter.StoryAdapter
import com.example.yourstory.viewmodel.story.StoryViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
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

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyStoriesResponse = DataDummy.generateDummyStories()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StoryViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getStories Should Not Null and Return Success`() = runTest {

        val testCoroutineScope = TestScope()

        testCoroutineScope.launch(Dispatchers.IO) {
            viewModel.storyPagingFlow.collectLatest {

                val differ = AsyncPagingDataDiffer(
                    diffCallback = StoryAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )

                Assert.assertNotNull(it)

                differ.submitData(it)

                Assert.assertNotNull(differ.snapshot())
                Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
                Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].id, differ.snapshot()[0]?.id)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].name, differ.snapshot()[0]?.name)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].description, differ.snapshot()[0]?.description)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].photoUrl, differ.snapshot()[0]?.photoUrl)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].createdAt, differ.snapshot()[0]?.createdAt)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].lat, differ.snapshot()[0]?.lat)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].lon, differ.snapshot()[0]?.lon)

            }
        }

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

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
