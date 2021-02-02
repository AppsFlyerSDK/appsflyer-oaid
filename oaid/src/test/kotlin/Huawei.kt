import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appsflyer.oaid.OaidClient
import com.huawei.hms.ads.identifier.AdvertisingIdClient
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowBuild
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.system.measureTimeMillis

@Config(shadows = [Huawei.ShadowAdvertisingIdClient::class])
@RunWith(AndroidJUnit4::class)
class Huawei {
    companion object {
        const val id = 42.toString()
    }

    init {
        ShadowBuild.setBrand("Huawei")
    }

    @Test
    fun ok() {
        ShadowAdvertisingIdClient.delay = 0
        OaidClient(getApplicationContext())
            .fetch()!!
            .let {
                Assert.assertEquals(id, it.id)
                Assert.assertFalse(it.lat!!)
            }
    }

    @Test
    fun timeout() {
        val delay = 2L
        ShadowAdvertisingIdClient.delay = delay
        val timeout = delay / 2
        measureTimeMillis {
            OaidClient(getApplicationContext(), timeout, SECONDS)
                .fetch()
                .let { Assert.assertNull(it) }
        }.let { Assert.assertTrue(SECONDS.toMillis(timeout) < it && it < SECONDS.toMillis(delay)) }
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    @Implements(AdvertisingIdClient::class)
    class ShadowAdvertisingIdClient {
        companion object {
            var delay = 0L

            @JvmStatic
            @Implementation
            fun isAdvertisingIdAvailable(context: Context) = true

            @JvmStatic
            @Implementation
            fun getAdvertisingIdInfo(context: Context): AdvertisingIdClient.Info {
                SECONDS.sleep(delay)
                return AdvertisingIdClient.Info(id, false)
            }
        }
    }
}