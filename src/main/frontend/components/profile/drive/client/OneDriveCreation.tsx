import {TFunction} from "i18next";
import {FormRuleModel} from "@hi-ui/form/lib/types/types";
import React from "react";
import {FormItem, Input, Select} from "@hi-ui/hiui";
import {ClientCreationContentProp, ClientType} from "../ClientCreationDialog";
import {useTranslation} from "react-i18next";

export const ClientCreationForm = (t: TFunction<"translate", undefined>) => {
    return new Map<ClientType, Record<string, FormRuleModel[]>>([
        ["OneDrive", {
            "azure_client_id": [
                {
                    required: true,
                    type: "string",
                    message: t("profile_drive_creation_OneDrive_client_id_empty")
                }
            ],
            "azure_client_secret": [
                {
                    required: true,
                    type: "string",
                    message: t("profile_drive_creation_OneDrive_client_secret_empty")
                }
            ],
            "tenant_id": [
                {
                    required: false,
                    type: "string",
                    message: t("profile_drive_creation_OneDrive_tenant_id_empty")
                }
            ],
            "end_point": [
                {
                    required: false,
                    type: "string",
                    message: t("profile_drive_creation_OneDrive_end_point_empty")
                }
            ],
        }]
    ])
}

export const OneDriveClientCreationContent = (props: ClientCreationContentProp) => {
    const { t } = useTranslation()
    return (
        <>
            <FormItem
                required={true}
                field={["data", "azure_client_id"]}
                label={t("profile_drive_creation_OneDrive_client_id")}>
                <Input disabled={props.isEdit || props.loading} />
            </FormItem>
            <FormItem
                required={true}
                field={["data", "azure_client_secret"]}
                label={t("profile_drive_creation_OneDrive_client_secret")}>
                <Input disabled={props.loading} />
            </FormItem>
            <FormItem
                field={["data", "tenant_id"]}
                label={t("profile_drive_creation_OneDrive_tenant_id")}
                formMessage={t("profile_drive_creation_OneDrive_tenant_id_hint")}>
                <Input disabled={props.isEdit || props.loading} />
            </FormItem>
            <FormItem
                required={true}
                field={["data", "end_point"]}
                label={t("profile_drive_creation_OneDrive_end_point")}>
                <Select
                    clearable={false}
                    disabled={props.isEdit || props.loading}
                    data={Array.from(OneDriveEndPointSelection).map(([key, _]) => {
                        return {
                            title: t("profile_drive_creation_OneDrive_end_point_" + key),
                            id: key,
                        }
                    })}/>
            </FormItem>
        </>
    )
}

type OneDriveEndPoint = "Global" | "CN"
const OneDriveEndPointSelection = new Map<OneDriveEndPoint, undefined>([
    ["Global", undefined],
    ["CN", undefined],
])