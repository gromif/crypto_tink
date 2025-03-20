package io.gromif.crypto.tink.data

import com.google.crypto.tink.KeysetHandle
import io.gromif.crypto.tink.model.KeysetFactory
import io.gromif.crypto.tink.model.KeysetTemplates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

private const val tag = "test_tag"
private val associatedData = "test_tag_ad".toByteArray()
private val params = KeysetTemplates.AEAD.AES128_GCM.params
class KeysetManagerTest {
    private val keysetFactoryMock: KeysetFactory = mockk()
    private val associatedDataManagerMock: AssociatedDataManager = mockk()
    private val keysetManager = KeysetManager(
        keysetFactory = keysetFactoryMock,
        associatedDataManager = associatedDataManagerMock
    )

    @Test
    fun getKeyset_NullAssociatedData_ShouldUseGlobalAssociatedData() {
        coEvery { associatedDataManagerMock.getAssociatedData() } returns ByteArray(0)

        coEvery { keysetFactoryMock.create(any(), any(), any()) } returns KeysetHandle.generateNew(params)

        runBlocking {
            keysetManager.getKeyset(tag, null, params)
            keysetManager.getKeyset(tag, associatedData, params)
        }

        coVerify(exactly = 1) { associatedDataManagerMock.getAssociatedData() }
    }

}