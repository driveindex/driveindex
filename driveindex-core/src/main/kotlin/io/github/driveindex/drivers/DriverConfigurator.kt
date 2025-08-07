package io.github.driveindex.drivers

import com.vaadin.flow.component.WebComponentExporter
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.webcomponent.WebComponent
import io.github.driveindex.drivers.api.ClientType

abstract class DriveClientConfigurator(
    protected val type: ClientType,
): WebComponentExporter<FormLayout>("DriveClient-Config-${type}") {
    override fun configureInstance(webComponent: WebComponent<FormLayout>, component: FormLayout) {

    }
}

abstract class DriveAccountConfigurator(
    protected val type: ClientType,
): WebComponentExporter<Div>("DriveClient-Config-${type}") {
    override fun configureInstance(webComponent: WebComponent<Div>, component: Div) {

    }
}
