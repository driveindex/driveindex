@file:JsModule("@hi-ui/button")

package hi_ui.button

import hi_ui.core.types.HiBaseHTMLProps
import react.dom.html.*
import web.html.HTMLAnchorElement
import web.html.HTMLButtonElement

/**
 * @author Madray Haven
 * @Date 2023/8/22 13:27
 */

external val Button: react.FC<ButtonProps>

external interface ButtonProps: ButtonHTMLAttributes<HTMLButtonElement> {
    var type: ButtonPropsType?
    var size: ButtonPropsSize?
    var appearance: ButtonPropsAppearance?
    var loading: Boolean?
    var href: String?
    var target: ButtonPropsTarget?
    var icon: react.ReactNode?
    var shape: react.ReactNode?
}

@JsName(
// language=JavaScript
    """
(/*union*/{primary: 'primary', success: 'success', danger: 'danger', default: 'default', secondary: 'secondary'}/*union*/)
"""
)
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
sealed external interface ButtonPropsType {
    companion object {
        val primary: ButtonPropsType
        val success: ButtonPropsType
        val danger: ButtonPropsType
        val default: ButtonPropsType
        val secondary: ButtonPropsType
    }
}

@JsName(
// language=JavaScript
    """
(/*union*/{lg: 'lg', sm: 'sm', md: 'md', xl: 'xl'}/*union*/)
"""
)
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
sealed external interface ButtonPropsSize {
    companion object {
        val lg: ButtonPropsSize
        val sm: ButtonPropsSize
        val md: ButtonPropsSize
        val xl: ButtonPropsSize
    }
}

@JsName(
// language=JavaScript
    """
(/*union*/{filled: 'filled', link: 'link', line: 'line', unset: 'unset'}/*union*/)
"""
)
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
sealed external interface ButtonPropsAppearance {
    companion object {
        val filled: ButtonPropsAppearance
        val link: ButtonPropsAppearance
        val line: ButtonPropsAppearance
        val unset: ButtonPropsAppearance
    }
}

@JsName(
// language=JavaScript
    """
    (/*union*/{_self: '_self', _blank: '_blank', _parent: '_parent', _top: '_top'}/*union*/)
"""
)
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
sealed external interface ButtonPropsTarget {
    companion object {
        val _self: ButtonPropsTarget
        val _blank: ButtonPropsTarget
        val _parent: ButtonPropsTarget
        val _top: ButtonPropsTarget
    }
}

@JsName(
// language=JavaScript
    """
    (/*union*/{square: 'square', round: 'round'}/*union*/)
"""
)
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
sealed external interface ButtonPropsShape {
    companion object {
        val square: ButtonPropsShape
        val round: ButtonPropsShape
    }
}