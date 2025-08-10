import axios from "axios";
import {UserPref} from "./prefs/UserPref";

/**
 * @deprecated
 */
export const DriveIndexAPI =  axios.create({
    baseURL: "http://localhost:11511",
    validateStatus: (status) => status !== 404
})

DriveIndexAPI.interceptors.request.use(
    (req) => {
        if (req.url !== "/api/login") {
            req.headers["Authorization"] = "Bearer "
        }
        return req
    }
)

/**
 * @deprecated
 */
export enum DriveType {
    OneDrive = "OneDrive",
}
