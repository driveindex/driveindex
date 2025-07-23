import React, {Dispatch, SetStateAction, useState} from "react";
import {Alert, Form, FormHelpers, FormItem, FormSubmit, Input, Modal} from "@hi-ui/hiui";
import {DriveIndexAPI} from "Frontend/core/axios";
import {useNavigate} from "react-router-dom";
import {key, translate} from "@vaadin/hilla-react-i18n";

const ProfilePassword = () => {
    const navigate = useNavigate()

    const [ loginDoing, setLoginDoing] = useState(false)
    const [ alert, setAlert ] = useState<string | null>(null)

    const formRef = React.useRef<FormHelpers>(null)
    const [ formData, setFormData ] = React.useState<any>({
        currentPassword: "",
        newPassword: "",
        newPasswordConfirm: "",
    })

    return (
        <>
            <h2>{translate(key`profile.password.title`)}</h2>
            <p>{translate(key`profile.password.desc`)}</p>
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
                title={translate(key`profile.password.success.title`)}
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
                onValuesChange={(_, allValue) => setFormData(allValue)}>
                <FormItem
                    label={<h3>{translate(key`profile.password.current`)}</h3>}
                    field={"currentPassword"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule, value, callback) => {
                                if (!value) {
                                    callback(translate(key`profile.password.error.currentEmpty`))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]}>
                    <div>
                        <p style={{marginTop: 0}}>{translate(key`profile.password.current.desc`)}</p>
                        <Input type={"password"} disabled={loginDoing} />
                    </div>
                </FormItem>
                <FormItem
                    label={<h3>{translate(key`profile.password.new`)}</h3>}
                    field={"newPassword"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule, value, callback) => {
                                const passwordReg = /^(?![^a-zA-Z]+$)(?!D+$).{8,16}$/
                                if (!value) {
                                    callback(translate(key`profile.password.error.empty`))
                                } else if (!passwordReg.test(value)) {
                                    callback(translate(key`profile.password.error.format`))
                                } else {
                                    callback()
                                }
                            }
                        }
                    ]}>
                    <Input type={"password"} disabled={loginDoing} />
                </FormItem>
                <FormItem
                    label={<h3>{translate(key`profile.password.newConfirm`)}</h3>}
                    field={"newPasswordConfirm"}
                    valueType={"string"}
                    rules={[
                        {
                            validator: (rule, value, callback) => {
                                if (!value) {
                                    callback(translate(key`profile.password.error.confirmEmpty`))
                                } else if (value !== formData.newPassword) {
                                    callback(translate(key`profile.password.error.confirm`))
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
                            setAlert, setLoginDoing
                        )
                    }}
                    loading={loginDoing}
                    disabled={loginDoing}>
                    {translate(key`profile.password.save`)}
                </FormSubmit>
            </Form>
        </>
    )
}

function submitPasswordChange(
    currentPassword: string,
    newPassword: string,
    showAlert: Dispatch<SetStateAction<string | null>>,
    setLoading: Dispatch<SetStateAction<boolean>>,
) {
    setLoading(true)
    setTimeout(() => {
        DriveIndexAPI.post("/api/user/password", {
            old_pwd: currentPassword,
            new_pwd: newPassword,
        }).then(value => {
            if (value.data["code"] !== 200) {
                showAlert(translate(key`profile.password.error.edit`) + value.data["message"])
            } else {

            }
        }).finally(() => {
            setLoading(false)
        })
    }, 200)
}

export default ProfilePassword
