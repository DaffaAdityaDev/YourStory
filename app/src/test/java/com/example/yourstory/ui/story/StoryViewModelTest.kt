import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseData>() {
//            override fun areItemsTheSame(
//                oldItem: StoryResponseData,
//                newItem: StoryResponseData
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun areContentsTheSame(
//                oldItem: StoryResponseData,
//                newItem: StoryResponseData
//            ): Boolean {
//                TODO("Not yet implemented")
//            }
//        }
//    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


//    @get:Rule
//    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository
    @Mock
    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: StoryViewModel

//    private val lifecycleOwner = FakeLifecycleOwner()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = createTestCoroutineScope(testDispatcher)

    private val dummyStoriesResponse = DataDummy.generateDummyStories()

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
    fun `when getStories Should Not Null and Return Success`() = runTest {

        val testCoroutineScope = TestCoroutineScope()

//        val data: PagingData<StoryResponseData> = StoryPagingSource.snapshot(dummyStoriesResponse.listStory)
//        val expectedStories = MutableLiveData<PagingData<StoryResponseData>>()
//        expectedStories.value = data

//        val response = Response.success(dummyStoriesResponse)
//
//        val token = viewModel.getToken() ?: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTZkbUJHMFh1Wlk1UU5NdlkiLCJpYXQiOjE2ODM0Nzc5NzV9.MZPnd9v9Cpipg5HbMr9CkeA1UZENHQ-jq8LugXSmDiM"
////        Mockito.`when`(repository.getStories(token)).thenReturn(expectedStories)
//
//        val listStoryViewModel = StoryViewModel(repository, sessionManager, testCoroutineScope)
//        var actualStories: Flow<PagingData<StoryResponseData>>? = null

        testCoroutineScope.launch(Dispatchers.IO) {
//            doReturn(response).`when`(repository).getAllStories(token, null, null, null)
            viewModel.storyPagingFlow.collectLatest {

                val differ = AsyncPagingDataDiffer(
                    diffCallback = StoryAdapter.DIFF_CALLBACK,
                    updateCallback = noopListUpdateCallback,
                    workerDispatcher = Dispatchers.Main,
                )
                differ.submitData(it)

                Assert.assertNotNull(it)
                Assert.assertNotNull(differ.snapshot())
                Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
                Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
                Assert.assertEquals(dummyStoriesResponse.listStory[0].id, differ.snapshot()[0]?.id)
            }
//            actualStories = listStoryViewModel.storyPagingFlow
//
//            actualStories?.collectLatest {
//                println("test - actualStories: ${it}")
//                println("test - expectedStories: ${expectedStories.value}")
//
//                Assert.assertNotNull(it)
//                Assert.assertEquals(expectedStories.value, it)
//            }
        }

//        println("test - actualStories: ${actualStories}")
//
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = StoryAdapter.DIFF_CALLBACK,
//            updateCallback = noopListUpdateCallback,
//            workerDispatcher = Dispatchers.Main,
//        )

//        submitDataAndWaitForSnapshot(differ, actualStories)
//
//        println("test - actualStories: ${differ.snapshot().items}")
//        println("test - actualStories: ${differ.snapshot().items.size}")
//        println("test - expectedStories: ${expectedStories.value}")
//
//
//        Assert.assertNotNull(differ.snapshot())
//        Assert.assertEquals(dummyStoriesResponse.listStory, differ.snapshot())
//        Assert.assertEquals(dummyStoriesResponse.listStory.size, differ.snapshot().size)
//        Assert.assertEquals(dummyStoriesResponse.listStory[0].id, differ.snapshot()[0]?.id)
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




//suspend fun submitDataAndWaitForSnapshot(
//    differ: AsyncPagingDataDiffer<StoryResponseData>,
//    data: PagingData<StoryResponseData>
//) {
//    val latch = CountDownLatch(1)
//    differ.addLoadStateListener { loadStates ->
//        if (loadStates.refresh is LoadState.NotLoading) {
//            latch.countDown()
//        }
//    }
//    differ.submitData(data)
//    withContext(Dispatchers.IO) {
//        latch.await(5, TimeUnit.SECONDS)
//    }
//}


fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}


class StoryPagingSource : PagingSource<Int, LiveData<List<StoryResponseData>>>() {
    companion object {
        fun snapshot(items: List<StoryResponseData>): PagingData<StoryResponseData> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponseData>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponseData>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
class DiffFavoriteEventCallback : DiffUtil.ItemCallback<StoryResponseData>() {
    override fun areItemsTheSame(
        oldItem: StoryResponseData,
        newItem: StoryResponseData
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: StoryResponseData,
        newItem: StoryResponseData
    ): Boolean {
        return oldItem == newItem
    }
}


//class TestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    val name: TextView = view.findViewById(R.id.story_name)
//    val description: TextView = view.findViewById(R.id.story_description)
//    val photo: ImageView = view.findViewById(R.id.story_photo)
//}


//class TestAdapter : PagingDataAdapter<StoryResponseData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponseData>() {
//            override fun areItemsTheSame(oldItem: StoryResponseData, newItem: StoryResponseData): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: StoryResponseData, newItem: StoryResponseData): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        // Implement the view holder creation
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.story_item, parent, false)
//        return TestViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        // Implement the view binding
//        if (holder is TestViewHolder) {
//            val currentItem = getItem(position) ?: return
//            holder.name.text = currentItem.name
//            holder.description.text = currentItem.description
//
//            Glide.with(holder.photo.context)
//                .load(currentItem.photoUrl)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.photo)
//
//            holder.itemView.setOnClickListener {
//                val user = StoryResponseData(
//                    currentItem.id,
//                    currentItem.name,
//                    currentItem.description,
//                    currentItem.photoUrl,
//                    currentItem.createdAt,
//                    currentItem.lat,
//                    currentItem.lon
//                )
//                val intent = Intent(holder.itemView.context, DetailStory::class.java)
//                intent.putExtra("storyDetail", user)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            val fadeIn = AlphaAnimation(0.0f, 1.0f)
//            fadeIn.duration = 500
//            holder.itemView.startAnimation(fadeIn)
//        }
//    }
//}
//
//class FakeLifecycleOwner : LifecycleOwner {
//    private val lifecycleRegistry = LifecycleRegistry(this)
//
//    init {
//        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
//        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
//        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    }
//
//    override val lifecycle: Lifecycle
//        get() = lifecycleRegistry
//}
