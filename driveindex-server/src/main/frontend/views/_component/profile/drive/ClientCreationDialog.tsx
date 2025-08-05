import React, {useEffect, useState} from "react";
import {
    Button,
    Form,
    FormHelpers,
    FormItem,
    FormSubmit,
    Input,
    Modal,
    Row,
    Select
} from "@hi-ui/hiui";
import {FormRuleModel} from "@hi-ui/form";
import {useClientCreationForm, OneDriveClientCreationContent} from "./client/OneDriveCreation";
import {i18n, key, translate} from "@vaadin/hilla-react-i18n";
import ClientType from "Frontend/generated/io/github/driveindex/core/client/ClientType";

const ClientCreationDialog = (props: {
    visible: boolean,
    requestClose: (created: boolean) => void,
    initValue?: any,
}) => {
    const isEdit = props.initValue !== undefined

    const [ loading, setLoading ] = useState(false)

    const [ type, setType ] = useState<ClientType | undefined>()
    const ClientCreationContentImpl = ClientCreationContent()
    const CreationContent = type === undefined ? undefined : ClientCreationContentImpl.get(type)

    const CreationFormOther = useClientCreationForm()
    const CreationFormBase = {
        "name": [
            {
                required: true,
                type: "string",
                message: translate(key`profile.drive.add.client.name.error.empty`)
            }
        ],
        "type": [
            {
                required: true,
                type: "string",
                message: translate(key`profile.drive.add.client.type.error.empty`)
            }
        ],
    }
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
                title={translate(key`profile.drive.add.client`)}
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
                        label={translate(key`profile.drive.add.client.name`)}>
                        <Input disabled={loading} />
                    </FormItem>
                    <FormItem
                        required={true}
                        field={"type"}
                        label={translate(key`profile.drive.add.client.type`)}>
                        <Select
                            clearable={false}
                            disabled={isEdit || loading}
                            data={Array.from(ClientCreationContentImpl).map(([name, _]) => {
                                return {
                                    title: i18n.translateDynamic(`profile.drive.add.client.OneDrive.${name}`),
                                    id: name,
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
                            {translate(key`common.cancel`)}
                        </Button>
                        <FormSubmit
                            onClick={(value, err) => {
                                if (err !== null) {
                                    return
                                }
                                setLoading(true)
                                submit(value)
                                    .then(() => {

                                    })
                                    .finally(() => setLoading(false))
                            }}
                            disabled={loading}
                            loading={loading}>
                            {isEdit ? translate(key`save`) : translate(key`profile.drive.add.client.action`)}
                        </FormSubmit>
                    </Row>
                </Form>
            </Modal>
        </>
    )
}

export interface ClientCreationContentProp {
    loading: boolean,
    isEdit: boolean,
}
// TODO: Using object-oriented
export const ClientCreationContent = () => {
    return new Map<ClientType, any>([
        [ClientType.OneDrive, OneDriveClientCreationContent],
    ])
}

async function submit(
    data: any,
) {
    // DriveIndexAPI.post("/api/user/client", data).then(value => {
    //     if (value.data["code"] !== 200) {
    //         requestFailed(value.data["message"])
    //     } else {
    //         requestSuccess(value.data["message"])
    //     }
    // }).catch((error) => {
    //     requestFailed(error.message)
    // }).finally(() => {
    //     setLoading(false)
    // })
}


export default ClientCreationDialog