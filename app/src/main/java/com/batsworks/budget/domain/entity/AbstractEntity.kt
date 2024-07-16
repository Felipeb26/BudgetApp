package com.batsworks.budget.domain.entity

import com.google.firebase.firestore.DocumentId

abstract class AbstractEntity(@DocumentId open val id: String = "")