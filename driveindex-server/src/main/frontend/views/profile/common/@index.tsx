import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

export const config: ViewConfig = {
    loginRequired: true,
};

const ProfileCommon = () => {
    return (
        <>
            <div>
                ProfileCommonFragment
            </div>
        </>
    )
}

export default ProfileCommon
