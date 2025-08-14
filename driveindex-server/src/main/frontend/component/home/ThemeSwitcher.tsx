import {useEffect, useState} from "react";
import {UserPref} from "Frontend/core/prefs/UserPref";
import {Icon} from "@vaadin/react-components";

const ThemeSwitcher = () => {
    const [dark, setDark] = useState(UserPref.DarkTheme);
    useEffect(() => {
        UserPref.DarkTheme = dark
        if (dark) {
            document.documentElement.setAttribute("theme", "dark");
        } else {
            document.documentElement.setAttribute("theme", "light");
        }
    }, [dark])
    return (
        <Icon icon={dark ? "vaadin:sun-o" : "vaadin:moon-o"} onClick={() => setDark(!dark)} />
    )
}

export default ThemeSwitcher
