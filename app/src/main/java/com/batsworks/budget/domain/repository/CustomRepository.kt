package com.batsworks.budget.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class CustomRepository<T>(
	private val collection: String,
	private val type: Class<T>,
	private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {

	private val _fullList = MutableStateFlow<MutableList<T>>(mutableListOf())
	private val fullList: StateFlow<MutableList<T>> = _fullList

	fun save(t: T): Task<Void> {
		return db.collection(collection).document().set(t as Any)
	}

	fun findAll(): StateFlow<MutableList<T>> {
		val list: MutableList<T> = mutableListOf()
		db.collection(collection).get().addOnCompleteListener { query ->
			if (query.isSuccessful) {
				for (documents in query.result) {
					val document = documents.toObject(type)
					list.add(document)
					_fullList.value = list
				}
			}
		}
		return fullList
	}

	suspend fun findByDocument(document: String): DocumentSnapshot {
		return db.collection(collection).document(document).get().await()
	}

	suspend fun delete(document: String): Void {
		return db.collection(collection).document(document).delete().await()
	}

	suspend fun update(data: MutableMap<String, Any>): Void {
		return db.collection(collection).document().update(data).await()
	}

	suspend fun findByParam(param: String, value: Any): QuerySnapshot {
		return db.collection(collection).whereEqualTo(param, value).get().await()
	}

	suspend fun findByParam(filter: Filter): QuerySnapshot {
		return db.collection(collection).where(filter).get().await()
	}
}