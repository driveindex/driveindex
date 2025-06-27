import React, {createContext, FC, useContext, useEffect, useState} from "react";

// https://mui.com/material-ui/customization/breakpoints/#default-breakpoints
type BreakpointEnum = "xs" | "sm" | "md" | "lg" | "xl"

export class Breakpoint {
    private static readonly order = ["xs", "sm", "md", "lg", "xl"]

    private readonly index: number
    constructor(value: BreakpointEnum) {
        this.index = Breakpoint.order.indexOf(value)
    }

    up(other: BreakpointEnum) {
        return Breakpoint.order.indexOf(other) <= this.index
    }
    down(other: BreakpointEnum) {
        return Breakpoint.order.indexOf(other) >= this.index
    }
}

const ViewportContext: React.Context<{
    width: number
}> = createContext({
    width: window.innerWidth
})

export const ViewportProvider: FC<{ children: React.ReactNode }> = (props) => {
    const [ width, setWidth ] = useState(window.innerWidth)

    const handleWindowResize = () => {
        setWidth(window.innerWidth)
    }
    useEffect(() => {
        window.addEventListener("resize", handleWindowResize)
        return () => window.removeEventListener("resize", handleWindowResize)
    }, []);

    return (
        <ViewportContext.Provider value={{width: width}}>
            {props.children}
        </ViewportContext.Provider>
    )
}


const breakpointWidths = {
    "xl": 1536,
    "lg": 1200,
    "md": 900,
    "sm": 600,
    "xs": 0,
}


export const useBreakpointUp = (value: BreakpointEnum) => {
    const { width } = useContext(ViewportContext)
    return width > breakpointWidths[value]
}
export const useBreakpointDown = (value: BreakpointEnum) => {
    const { width } = useContext(ViewportContext)
    return width < breakpointWidths[value]
}