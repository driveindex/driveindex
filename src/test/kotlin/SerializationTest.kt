import io.github.driveindex.core.util.JsonGlobal
import io.github.driveindex.core.util.encodeWithoutClassDiscriminator
import io.github.driveindex.dto.resp.AccountsDto
import org.junit.jupiter.api.Test
import java.util.*

class SerializationTest {
    @Test
    fun respResultData() {
        println(JsonGlobal.encodeWithoutClassDiscriminator(
                AccountsDto(
                        id = UUID.randomUUID(),
                        displayName = "test",
                        userPrincipalName = "user",
                        createAt = System.currentTimeMillis(),
                        modifyAt = System.currentTimeMillis(),
                        detail = AccountsDto.OneDriveAccountDetail(
                                azureUserId = "test-id"
                        )
                )
        ))
    }
}