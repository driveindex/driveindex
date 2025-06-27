import React, {FC} from "react";
import {useTranslation} from "react-i18next";
import {Modal} from "@hi-ui/hiui";


const AccountCreationDialog: FC<{
    visible: boolean,
    type: string,
    requestClose: () => void,
}> = (props) => {
    const { t } = useTranslation()

    const Content = AccountCreationContent.get(props.type)
    return (
        <Modal
            visible={props.visible}
            title={t("profile_drive_add_account")}
            onCancel={() => props.requestClose()}
            closeable={false}>
            {Content != undefined && <Content />}
        </Modal>
    )
}

const AccountCreationContent: Map<string, FC> = new Map<string, React.FC<any>>([
    ["OneDrive", (props) => {
        return (
            <>
            </>
        )
    }],
])

export default AccountCreationDialog