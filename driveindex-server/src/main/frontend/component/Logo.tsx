import {useNavigate} from "react-router-dom";
import React from "react";
import {key, translate} from '@vaadin/hilla-react-i18n';

export default function Logo(prop: { style?: React.CSSProperties }) {
    const navigate = useNavigate();
    return (
        <div style={{ display: "flex", flexDirection: "row", alignItems: "center", ...prop.style }}>
            <img style={{ width: 34, height: 34 }} src={"/drawable/logo.svg"} alt={"logo"} onClick={() => navigate("/")}/>
            <div style={{ marginLeft: 10, color: "#1f2733" }}><strong>{translate(key`common.title`)}</strong></div>
        </div>
    )
}