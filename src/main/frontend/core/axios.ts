import axios from "axios";
import {UserPref} from "./prefs/UserPref";

const isDev = (process.env.NODE_ENV === "development")

export const DriveIndexAPI =  axios.create({
    baseURL: isDev ? (process.env.REACT_APP_BASE_API ?? "http://localhost:11511") : undefined,
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
