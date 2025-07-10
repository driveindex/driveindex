import { configureAuth } from '@vaadin/hilla-react-auth';
import { UserConfController } from "Frontend/generated/endpoints";

const auth = configureAuth(UserConfController.getUserInfo)
export const useAuth = auth.useAuth
export const AuthProvider = auth.AuthProvider