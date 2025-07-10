export class UserPref {
    static get Login(): boolean {
        return UserPref.getBoolOrDef("Login", false)
    }
    static set Login(value: boolean) {
        localStorage["Login"] = value
    }

    static get Username(): string {
        return UserPref.getStrOrDef("Username", "")
    }
    static set Username(value: string) {
        localStorage["Username"] = value
    }

    static get AccessToken(): string {
        return UserPref.getStrOrDef("AccessToken", "")
    }
    static set AccessToken(value: string) {
        localStorage["AccessToken"] = value
    }

    static get Role(): "ADMIN" | "USER" | undefined {
        const role = UserPref.getStrOrDef("Nick", "")
        if (role === "ADMIN" || role === "USER") {
            return role as "ADMIN" | "USER" | undefined
        } else {
            return undefined
        }
    }
    static set Role(value: string)  {
        localStorage["Role"] = value
    }

    static get Nick(): string {
        return UserPref.getStrOrDef("Nick", "")
    }
    static set Nick(value: string)  {
        localStorage["Nick"] = value
    }

    private static getStrOrDef(key: string, def: string): string {
        return localStorage[key] ?? def
    }
    private static getBoolOrDef(key: string, def: boolean): boolean {
        const value = localStorage[key]
        if (value == null) {
            return def
        } else {
            return value === "true"
        }
    }
}