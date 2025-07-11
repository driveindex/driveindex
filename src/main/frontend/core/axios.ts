import axios from "axios";
import {UserPref} from "./prefs/UserPref";

export const DriveIndexAPI =  axios.create({
    baseURL: "http://localhost:11511",
    validateStatus: (status) => status !== 404
})

DriveIndexAPI.interceptors.request.use(
    (req) => {
        if (req.url !== "/api/login") {
            req.headers["Authorization"] = "Bearer " + UserPref.AccessToken
        }
        return req
    }
)

export enum DriveType {
    OneDrive = "OneDrive",
}
