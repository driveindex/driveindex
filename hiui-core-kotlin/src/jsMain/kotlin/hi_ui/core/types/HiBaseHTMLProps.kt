package hi_ui.core.types

import react.ElementType

/**
 * @author Madray Haven
 * @Date 2023/8/22 13:54
 */
external interface HiBaseHTMLProps<T: ElementType<*>, P: Any>: react.Props {
    var prefixCls: String?
    var role: String?
}