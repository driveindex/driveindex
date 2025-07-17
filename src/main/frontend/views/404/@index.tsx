import {CommonHeader} from "Frontend/views/_component/home/CommonHeader";
import {EmptyState} from "@hi-ui/hiui";
import {useTranslation} from "react-i18next";

const NotFoundPage = () => {
    const { t } = useTranslation()
    return (
        <div>
            <CommonHeader isShowInProfile={false} showAvatar={false} />
            <EmptyState
                style={{
                    marginTop: 100,
                }}
                size={"lg"}
                title={t("not_found")} />
        </div>
    )
}

export default NotFoundPage