import React, {FC} from "react";
import {key, translate} from '@vaadin/hilla-react-i18n';
import {Modal} from "@hi-ui/hiui";


const AccountCreationDialog = (props: {
    visible: boolean,
    type: string,
    requestClose: () => void,
}) => {
    const Content = AccountCreationContent.get(props.type)
    return (
        <Modal
            visible={props.visible}
            title={translate(key`profile.drive.add.account`)}
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