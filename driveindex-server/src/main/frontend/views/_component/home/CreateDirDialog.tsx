import {Button, Dialog, FormLayout, TextField, TextFieldValueChangedEvent} from "@vaadin/react-components";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {useState} from "react";

interface CreateDirDialogProps {
    visible: boolean
    currentPath: string
    onClose: (created: boolean) => void
}

const CreateDirDialog = (props: CreateDirDialogProps) => {
    const [nameError, setNameError] = useState<string>()
    const [name, setName] = useState<string>()

    const createDirAction = () => {

    }

    return (
        <Dialog
            opened={props.visible}
            headerTitle={translate(key`home.file.create.dir`)}
            onOpenedChanged={() => props.onClose(false)}
            footer={
                <>
                    <Button onClick={() => props.onClose(false)}>{
                        translate(key`common.cancel`)
                    }</Button>
                    <Button onClick={() => createDirAction()}>{
                        translate(key`home.file.create.action`)
                    }</Button>
                </>
            }>
            <FormLayout>
                <TextField
                    label={translate(key`home.file.create.name`)}
                    required={true}
                    errorMessage={nameError}
                    value={name}
                    onValueChanged={(e: TextFieldValueChangedEvent) => {
                        setName(e.detail.value)
                    }}/>
            </FormLayout>
        </Dialog>
    )
}

export default CreateDirDialog
