import React, {Context, createContext, FC, useContext, useState} from "react";
import {Modal} from "@hi-ui/hiui";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {AxiosResponse} from "axios";

const LoginExpiredContext: Context<{
    showLoginExpiredDialog?: () => void
}> = createContext({})

export const LoginExpired = (props: { children: React.ReactNode }) => {
    const [ loginExpiredDialog, setLoginExpiredDialog ] = useState(false)

    const { t } = useTranslation()
    const navigate = useNavigate()

    const showLoginExpiredDialog = () => {
        setLoginExpiredDialog(true)
    }

    return (
        <LoginExpiredContext.Provider value={{showLoginExpiredDialog: showLoginExpiredDialog}}>
            <Modal
                title={t("login_expire_title")}
                visible={loginExpiredDialog}
                closeable={false}
                cancelText={null}
                onConfirm={() => {
                    setLoginExpiredDialog(false)
                    navigate("/login")
                }}>
                <div>{t("login_expire_text")}</div>
            </Modal>
            {
                props.children
            }
        </LoginExpiredContext.Provider>
    )
}

export function checkLoginStatus(
    resp: AxiosResponse<any, any>,
    showLoginExpiredDialog: () => void,
) {
    if (resp.data["code"] === -110105) {
        showLoginExpiredDialog()
        return false
    } else {
        return true
    }
}

export function useLoginExpiredDialog() {
    const { showLoginExpiredDialog } = useContext(LoginExpiredContext)
    return showLoginExpiredDialog!!
}