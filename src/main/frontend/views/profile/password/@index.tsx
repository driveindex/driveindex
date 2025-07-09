import React, {Dispatch, FC, SetStateAction, useState} from "react";
import {useTranslation} from "react-i18next";
import {Alert, Form, FormHelpers, FormItem, FormSubmit, Input, Modal} from "@hi-ui/hiui";
import {TFunction} from "i18next";
import {DriveIndexAPI} from "Frontend/driveindex/axios";
import {checkLoginStatus, useLoginExpiredDialog} from "Frontend/driveindex/hooks/useLoginExpiredDialog";
import {useNavigate} from "react-router-dom";

const Index: FC = () => {
    const { t } = useTranslation()
    const navigate = useNavigate()

    const [ loginDoing, setLoginDoing] = useState(false)
    const [ alert, setAlert ] = useState<string | null>(null)
    const showLoginExpiredDialog = useLoginExpiredDialog()

    const formRef = React.useRef<FormHelpers>(null)
    const [ formData, setFormData ] = React.useState<any>({
        currentPassword: "",
        newPassword: "",
        newPasswordConfirm: "",
    })

    return (
        <>
            <h2>{t("profile_password_title")}</h2>
            <p>{t("profile_password_text")}</p>
            {
                alert !== null && (
                    <Alert
                        title={alert}
                        closeable={true}
                        type={"danger"}
                        onClose={() => {setAlert(null)}}/>
                )
            }
            <Modal
                title={t("profile_password_success_title")}
                closeable={false}
                cancelText={null}
                onConfirm={() => {
                    navigate("/login")
                }}
                type={"success"}>

            </Modal>
            <Form
                labelPlacement={"top"}
                innerRef={formRef}
                initialValues={{
                    currentPassword: "",
                    newPassword: "",
                    newPasswordConfirm: "",
                }}
                onValuesChange={(_: any, allValue: any) => setFormData(allValue)}>
                <FormItem
                    label={<h3>{t("profile_password_current")}</h3>}
                    field={"currentPassword"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule: any, value: any, callback: (arg0?: string) => void) => {
                                if (!value) {
                                    callback(t("profile_password_error_current_empty"))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]}>
                    <div>
                        <p style={{marginTop: 0}}>{t("profile_password_current_desc")}</p>
                        <Input type={"password"} disabled={loginDoing} />
                    </div>
                </FormItem>
                <FormItem
                    label={<h3>{t("profile_password_new")}</h3>}
                    field={"newPassword"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule: any, value: string, callback: (arg0?: string) => void) => {
                                const passwordReg = /^(?![^a-zA-Z]+$)(?!D+$).{8,16}$/
                                if (!value) {
                                    callback(t("profile_password_error_empty"))
                                } else if (!passwordReg.test(value)) {
                                    callback(t("profile_password_error_format"))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]}>
                    <Input type={"password"} disabled={loginDoing} />
                </FormItem>
                <FormItem
                    label={<h3>{t("profile_password_new_confirm")}</h3>}
                    field={"newPasswordConfirm"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule: any, value: any, callback: (arg0?: string) => void) => {
                                if (!value) {
                                    callback(t("profile_password_error_conform_empty"))
                                } else if (value !== formData.newPassword) {
                                    callback(t("profile_password_error_conform"))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]}>
                    <Input type={"password"} disabled={loginDoing} />
                </FormItem>
                <FormSubmit
                    onClick={(value: any, _: any) => {
                        if (value == null) {
                            return
                        }
                        submitPasswordChange(
                            formData.password,
                            formData.newPassword,
                            showLoginExpiredDialog,
                            setAlert, setLoginDoing, t
                        )
                    }}
                    loading={loginDoing}
                    disabled={loginDoing}>
                    {t("profile_password_save")}
                </FormSubmit>
            </Form>
        </>
    )
}

function submitPasswordChange(
    currentPassword: string,
    newPassword: string,
    showLoginExpiredDialog: () => void,
    showAlert: Dispatch<SetStateAction<string | null>>,
    setLoading: Dispatch<SetStateAction<boolean>>,
    t: TFunction<"translation", undefined>,
) {
    setLoading(true)
    setTimeout(() => {
        DriveIndexAPI.post("/api/user/password", {
            old_pwd: currentPassword,
            new_pwd: newPassword,
        }).then(value => {
            if (!checkLoginStatus(value, showLoginExpiredDialog)) {
                return
            }
            if (value.data["code"] !== 200) {
                showAlert(t("profile_password_error") + value.data["message"])
            } else {

            }
        }).catch(error => {
            showAlert(t("login_failed") + error.message)
        }).finally(() => {
            setLoading(false)
        })
    }, 200)
}

export default Index