package com.batsworks.budget.data.entity

import com.google.firebase.firestore.DocumentId

abstract class AbstractEntity(@DocumentId open val id: String = "")