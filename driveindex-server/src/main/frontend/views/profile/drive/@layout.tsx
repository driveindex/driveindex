import {VerticalLayout} from "@vaadin/react-components";
import {Outlet} from "react-router";

const DriveLayout = () => {
    return (
        <VerticalLayout>
            <Outlet />
        </VerticalLayout>
    )
}

export default DriveLayout
