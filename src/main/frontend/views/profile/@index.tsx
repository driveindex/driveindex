import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

export const config: ViewConfig = {
    loginRequired: true,
};

export default function ProfilePage() {
    return (
        <>
            <div>
                ProfileCommonFragment
            </div>
        </>
    )
}
