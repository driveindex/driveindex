import {useRef, useState} from "react";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {useNavigate} from "react-router-dom";
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";
import {useAuth} from "Frontend/core/security/auth"
import {asSha256} from "Frontend/core/util/_String";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {LoginForm, LoginFormLoginEvent, Notification} from "@vaadin/react-components";
import {LoginFormErrorChangedEvent} from "@vaadin/login/src/vaadin-login-form";

export const config: ViewConfig = {

};

const LoginPage = () => {
    const navigate = useNavigate()

    const [ alert, setAlert ] = useState<string>()

    const { login } = useAuth()
    const doLogin = (username: string, password: string) => {
        login(username, asSha256(password), {
            loginProcessingUrl: "/login",
            onSuccess: () => {
                navigate("/")
            },
        }).then((result) => {
            if (result.error) {
                const errorMsg = result.errorTitle!.replaceAll(".", "").replaceAll(" ", "_").toLowerCase()
                setAlert(translate(key([`login.error.${errorMsg}`])))
            }
        })
    }

    return (
        <div style={{
            display: "flex",
            alignItems: "center",
            flexDirection: "column",
            height: "100vh",
        }}>
            <LoginForm
                noAutofocus={true}
                noForgotPassword={true}
                style={{marginTop: 64}}
                error={alert !== undefined}
                i18n={{
                    form: {
                        title: translate(key`login.title`),
                        username: translate(key`login.username`),
                        password: translate(key`login.password`),
                        submit: translate(key`login.action`),
                    },
                    errorMessage: {
                        title: translate(key`login.error`),
                        message: alert,
                        username: translate(key`login.username.error.empty`),
                        password: translate(key`login.password.error.empty`),
                    },
                }}
                onLogin={(event: LoginFormLoginEvent) => {
                    doLogin(event.detail.username, event.detail.password)
                }}
                onErrorChanged={(event: LoginFormErrorChangedEvent) => {
                    if (!event.detail.value) {
                        setAlert(undefined)
                    }
                }}/>
        </div>
    )
}

export default LoginPage
