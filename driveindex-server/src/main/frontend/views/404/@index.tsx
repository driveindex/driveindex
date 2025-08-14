import {CommonHeader} from "Frontend/component/home/CommonHeader";
import {EmptyState} from "@hi-ui/empty-state";
import {key, translate} from "@vaadin/hilla-react-i18n";

const NotFoundPage = () => {
    return (
        <div>
            <CommonHeader isShowInProfile={false} showAvatar={false} />
            <EmptyState
                style={{
                    marginTop: 100,
                }}
                size={"lg"}
                title={translate(key`common.error.notFound`)} />
        </div>
    )
}

export default NotFoundPage