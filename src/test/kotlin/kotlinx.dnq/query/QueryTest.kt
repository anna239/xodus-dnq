package kotlinx.dnq.query

import kotlinx.dnq.DBTest
import kotlinx.dnq.transactional
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class QueryTest : DBTest() {

    @Test
    fun `firstOrNull should return null if nothing found`() {
        store.transactional {
            assertNull(User.all().firstOrNull())
        }
    }

    @Test
    fun `firstOrNull should return entity if something is there`() {
        store.transactional {
            User.new {
                login = "test"
                skill = 1
            }
            assertNotNull(User.all().firstOrNull())
        }
    }

    @Test
    fun `first should throw if nothing found`() {
        store.transactional {
            assertFailsWith<NoSuchElementException> {
                User.all().first()
            }
        }
    }

    @Test
    fun `first should return entity if something is there`() {
        store.transactional {
            User.new {
                login = "test"
                skill = 1
            }
            assertNotNull(User.all().firstOrNull())
        }
    }

    @Test
    fun `query should obey custom db names of link properties`() {
        store.transactional {
            User.new {
                login = "user1"
                skill = 5
                supervisor = User.new {
                    login = "boss"
                    skill = 555
                }
            }
        }

        store.transactional {
            assertEquals(1, User.query(User::supervisor ne null).size())
        }
    }
}