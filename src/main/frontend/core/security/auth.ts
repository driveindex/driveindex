import { configureAuth } from '@vaadin/hilla-react-auth';
import { UserConfController } from "Frontend/generated/endpoints";
import ObjRespResult from "Frontend/generated/io/github/driveindex/dto/resp/ObjRespResult";
import UserInfoRespDto from "Frontend/generated/io/github/driveindex/dto/resp/UserInfoRespDto";

const auth = configureAuth(UserConfController.getUserInfo, {
    getRoles(user: ObjRespResult<UserInfoRespDto>): readonly string[] {
        return [user.data!.role]
    }
})
export const useAuth = auth.useAuth
export const AuthProvider = auth.AuthProvider