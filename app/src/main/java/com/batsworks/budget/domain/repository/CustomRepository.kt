package com.batsworks.budget.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CustomRepository<T>(
	private val collection: String,
	private val type: Class<T>,
	private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
	private val storage: StorageReference = FirebaseStorage.getInstance().reference,
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

	suspend fun findByParam(param: String, value: Any): Task<QuerySnapshot> {
		return db.collection(collection).whereEqualTo(param, value).get()
	}

	fun findByParam(vararg filter: Filter): Task<QuerySnapshot> {
		val dbConst = db.collection(collection)
		filter.forEach { dbConst.where(it) }
		return dbConst.get()
	}

	fun saveFile(file: ByteArray, document: String = UUID.randomUUID().toString()): UploadTask {
		val fileReference = storage.child("comprovantes/$document")
		return fileReference.putBytes(file)
	}
}