package com.batsworks.budget.services.notification

sealed class NotificationDataCreation(val content: String) {
    data object CHANNEL : NotificationDataCreation("batsworks_budget_notification")
    data object NAME : NotificationDataCreation("Notification BatsWorks Budget")
    data object DESCRIPTION: NotificationDataCreation("A notification from you budget app")
}