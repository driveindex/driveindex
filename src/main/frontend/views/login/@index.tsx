import {useRef, useState} from "react";
import {useTranslation} from "react-i18next";
import {Form, Card, FormItem, Input, FormSubmit, FormHelpers, Alert} from "@hi-ui/hiui";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {useNavigate} from "react-router-dom";
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";
import {useAuth} from "Frontend/core/security/auth"
import {asSha256} from "Frontend/core/util/_String";

export const config: ViewConfig = {

};

export default function LoginPage() {
    const { t } = useTranslation()
    const navigate = useNavigate()

    const formRef = useRef<FormHelpers>(null)
    const [ loginDoing, setLoginDoing ] = useState(false)
    const [ alert, setAlert ] = useState<string | null>(null)

    const { login } = useAuth()
    const doLogin = async (username: string, password: string) => {
        const result = await login(username, asSha256(password), {
            loginProcessingUrl: "/login",
            onSuccess: () => {
                navigate("/")
            },
        })
        if (result.error) {
            const errorMsg = result.errorTitle!.replaceAll(".", "").replaceAll(" ", "_").toLowerCase()
            setAlert(t("login_failed") + t(`login_failed_${errorMsg}`))
        }
    }

    return (
        <div style={{
            display: "flex",
            alignItems: "center",
            flexDirection: "column",
            height: "100vh",
        }}>
            <img
                src={"/drawable/logo.svg"}
                alt={"logo"}
                style={{
                    width: 60,
                    height: 60,
                    marginTop: 36,
                    marginBottom: 24,
                }}/>
            <div style={{
                fontSize: 20,
            }}>{t("login_title")}</div>
            {
                alert !== null && (
                    <Alert
                        title={<div>{alert}</div>}
                        closeable={true}
                        type={"danger"}
                        onClose={() => {setAlert(null)}}
                        style={{
                            width: 310,
                            marginTop: 12,
                        }}/>
                )
            }
            <Card
                style={{
                    width: 310,
                    marginTop: alert ? 0 : 24,
                }}>
                <Form
                    initialValues={{ username: UserPref.Username, password: "" }}
                    labelWidth={80}
                    rules={{
                        username: [
                            {
                                required: true,
                                type: "string",
                                message: t("login_username_empty"),
                            },
                        ],
                        password: [
                            {
                                required: true,
                                type: "string",
                                message: t("login_password_empty"),
                            },
                        ],
                    }}
                    innerRef={formRef}>
                    <FormItem
                        field={"username"}
                        label={t("login_username")}
                        labelPlacement={"top"}
                        showColon={false}>
                        <Input disabled={loginDoing}/>
                    </FormItem>
                    <FormItem
                        field={"password"}
                        label={t("login_password")}
                        labelPlacement={"top"}
                        showColon={false}>
                        <Input type={"password"} disabled={loginDoing}/>
                    </FormItem>
                    <FormItem labelPlacement={"top"}>
                        <FormSubmit
                            type={"primary"}
                            onClick={(value, _) => {
                                if (value == null) {
                                    return
                                }
                                doLogin(value["username"], value["password"])
                            }}
                            style={{
                                width: "100%"
                            }}
                            loading={loginDoing}
                            disabled={loginDoing}>{
                            t("login_action")
                        }</FormSubmit>
                    </FormItem>
                </Form>
            </Card>
        </div>
    )
}
