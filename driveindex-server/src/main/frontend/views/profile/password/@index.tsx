import React, {useState} from "react";
import {key, translate} from "@vaadin/hilla-react-i18n";
import {useForm} from "@vaadin/hilla-react-form";
import SetPwdReqDtoModel from "Frontend/generated/io/github/driveindex/dto/req/user/SetPwdReqDtoModel";
import {FormItem, FormLayout, TextField} from "@vaadin/react-components";
import {TextFieldValidatedEvent} from "@vaadin/text-field/src/vaadin-text-field";

const ProfilePassword = () => {
    const { model, field } = useForm(SetPwdReqDtoModel)

    const [currentPwdErrMsg, setCurrentPwdErrMsg] = useState("")

    const resetPwd = () => {

    }

    return (
        <>
            <h2>{translate(key`profile.password.title`)}</h2>
            <p>{translate(key`profile.password.desc`)}</p>
            <FormLayout>
                {/*<TextField*/}
                {/*    label={translate(key`profile.password.current`)}*/}
                {/*    type={"password"}*/}
                {/*    required={true}*/}
                {/*    minlength={12}*/}
                {/*    maxlength={28}*/}
                {/*    errorMessage={currentPwdErrMsg}*/}
                {/*    onValidated={(event: TextFieldValidatedEvent) => {*/}
                {/*        setCurrentPwdErrMsg("")*/}
                {/*        const field = event.target as TextFieldElement;*/}
                {/*        const { validity } = field.inputElement as HTMLInputElement;*/}
                {/*        if (validity.valueMissing) {*/}
                {/*            setCurrentPwdErrMsg(translate(key`profile.password.current.error.empty`))*/}
                {/*            return*/}
                {/*        }*/}
                {/*        if (validity.tooShort || validity.tooLong) {*/}
                {/*            setCurrentPwdErrMsg(translate(key`profile.password.current.error.empty`))*/}
                {/*        }*/}
                {/*    }}*/}
                {/*    {...field(model.old_pwd)} />*/}
            </FormLayout>
        </>
    )
}

export default ProfilePassword
