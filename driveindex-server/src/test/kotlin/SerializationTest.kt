import io.github.driveindex.core.utils.JsonGlobal
import io.github.driveindex.core.utils.encodeWithoutClassDiscriminator
import io.github.driveindex.dto.resp.AccountDto
import org.junit.jupiter.api.Test
import java.util.*

class SerializationTest {
    @Test
    fun respResultData() {
        println(JsonGlobal.encodeWithoutClassDiscriminator(
                AccountDto(
                        id = UUID.randomUUID(),
                        displayName = "test",
                        userPrincipalName = "user",
                        createAt = System.currentTimeMillis(),
                        modifyAt = System.currentTimeMillis(),
                        detail = AccountDto.OneDriveAccountDetail(
                                azureUserId = "test-id"
                        )
                )
        ))
    }
}