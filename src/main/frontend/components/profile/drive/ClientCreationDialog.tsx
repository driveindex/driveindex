import React, {FC, useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {
    Button,
    Form,
    FormHelpers,
    FormItem,
    FormSubmit,
    Input,
    message,
    Modal,
    Row,
    Select,
    useModal
} from "@hi-ui/hiui";
import {SelectMergedItem} from "@hi-ui/select/lib/types/types";
import {FormRuleModel} from "@hi-ui/form/lib/types/types";
import {TFunction} from "i18next";
import {ClientCreationForm, OneDriveClientCreationContent} from "./client/OneDriveCreation";
import {DriveIndexAPI} from "Frontend/driveindex/axios";
import {UserPref} from "Frontend/driveindex/prefs/UserPref";


export type ClientType = "OneDrive"

const ClientCreationDialog: FC<{
    visible: boolean,
    requestClose: (created: boolean) => void,
    initValue?: any,
}> = (props) => {
    const { t } = useTranslation()

    const isEdit = props.initValue !== undefined

    const [ loading, setLoading ] = useState(false)

    const [ type, setType ] = useState<ClientType | undefined>()
    const ClientCreationContentImpl = ClientCreationContent()
    const CreationContent = type === undefined ? undefined : ClientCreationContentImpl.get(type)

    const CreationFormOther = ClientCreationForm(t)
    const CreationFormBase = CommonClientCreationForm(t)
    const CreationForm = { ...(type === undefined ? {} : CreationFormOther.get(type)), ...CreationFormBase} as Record<string, FormRuleModel[]>

    const formRef = React.useRef<FormHelpers>(null)
    const requestClose = (created: boolean) => {
        props.requestClose(created)
        formRef.current?.reset()
        formRef.current?.setFieldsValue((record) => {
            const initValue: Record<string, any> = {}
            for (const key in CreationForm) {
                initValue[key] = ''
            }
            return initValue
        })
        setType(undefined)
    }

    useEffect(() => {
        if (props.initValue !== undefined) {
            formRef.current?.setFieldsValue(props.initValue)
            const type = props.initValue["type"]
        }
    }, [props.initValue])

    return (
        <>
            <Modal
                visible={props.visible}
                title={t("profile_drive_add_client")}
                closeable={false}
                confirmText={null}
                cancelText={null}
                showFooterDivider={false}>
                <Form
                    initialValues={{}}
                    rules={CreationForm}
                    innerRef={formRef}
                    labelPlacement={"top"}
                    onValuesChange={(changed) => {
                        console.log("changed value: {}", changed)
                        const type = changed["type"]
                        if (type !== undefined) {
                            if (type !== "") {
                                setType(changed["type"])
                            } else {
                                setType(undefined)
                            }
                        }
                    }}>
                    <FormItem
                        required={true}
                        field={"name"}
                        label={t("profile_drive_creation_name")}>
                        <Input disabled={loading} />
                    </FormItem>
                    <FormItem
                        required={true}
                        field={"type"}
                        label={t("profile_drive_creation_type")}>
                        <Select
                            clearable={false}
                            disabled={isEdit || loading}
                            data={Array.from(ClientCreationContentImpl).map(([key, _]) => {
                                return {
                                    title: t("profile_drive_creation_" + key),
                                    id: key,
                                }
                            })}/>
                    </FormItem>
                    {
                        CreationContent !== undefined && <CreationContent
                            loading={loading}
                            isEdit={isEdit}/>
                    }
                    <Row style={{
                        justifyContent: "end"
                    }}>
                        <Button
                            onClick={() => {
                                requestClose(false)
                            }}
                            disabled={loading}>
                            {t("cancel")}
                        </Button>
                        <FormSubmit
                            onClick={(value, err) => {
                                if (err !== null) {
                                    return
                                }
                                submit(value, t, setLoading, () => {
                                    requestClose(true)
                                })
                            }}
                            disabled={loading}
                            loading={loading}>
                            {isEdit ? t("save") : t("profile_drive_creation_action")}
                        </FormSubmit>
                    </Row>
                </Form>
            </Modal>
        </>
    )
}

const CommonClientCreationForm = (t: TFunction<"translate", undefined>) => {
    return {
        "name": [
            {
                required: true,
                type: "string",
                message: t("profile_drive_creation_name_empty")
            }
        ],
        "type": [
            {
                required: true,
                type: "string",
                message: t("profile_drive_creation_type_empty")
            }
        ],
    } as Record<string, FormRuleModel[]>
}
export interface ClientCreationContentProp {
    loading: boolean,
    isEdit: boolean,
}
export const ClientCreationContent = () => {
    return new Map<ClientType, React.FC<ClientCreationContentProp>>([
        ["OneDrive", OneDriveClientCreationContent],
    ])
}

function submit(
    data: any,
    t: TFunction<"translation", undefined>,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    requestSuccess: () => void,
) {
    setLoading(true)
    setTimeout(() => {
        DriveIndexAPI.post("/api/user/client", data).then(value => {
            if (value.data["code"] !== 200) {
                message.open({
                    title: t("profile_drive_creation_failed") + value.data["message"],
                    type: "error",
                })
            } else {
                message.open({
                    title: t("profile_drive_creation_success") + value.data["message"],
                    type: "success",
                })
                requestSuccess()
            }
        }).catch((error) => {
            message.open({
                title: t("profile_drive_creation_failed") + error.message,
                type: "error",
            })
        }).finally(() => {
            setLoading(false)
        })
    }, 500)
}


export default ClientCreationDialog