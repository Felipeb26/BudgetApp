package com.batsworks.budget.domain.repository

import android.util.Log
import com.batsworks.budget.domain.dao.FirebaseCollection
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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
	private val collection: FirebaseCollection,
	private val type: Class<T>,
	private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
	private val storage: StorageReference = FirebaseStorage.getInstance().reference,
) {

	private val _fullList = MutableStateFlow<MutableList<T>>(mutableListOf())
	private val fullList: StateFlow<MutableList<T>> = _fullList


	fun save(data: T): Task<DocumentReference> {
		return db.collection(collection.path).add(data as Any)
	}

	private suspend fun findAll(): MutableList<DocumentSnapshot> {
		return db.collection(collection.path).get().await().documents
	}

	suspend fun findByDocument(document: String): T? {
		return db.collection(collection.path).document(document).get().await().toObject(type)
	}

	suspend fun delete(document: String): Void {
		return db.collection(collection.path).document(document).delete().await()
	}

	suspend fun update(data: MutableMap<String, Any>): Void {
		return db.collection(collection.path).document().update(data).await()
	}

	fun findByParam(param: String, value: Any): Task<QuerySnapshot> {
		return db.collection(collection.path).whereEqualTo(param, value).get()
	}

	fun findByLogin(querys: HashMap<String, Any>): Task<QuerySnapshot> {
		val reference = db.collection(collection.path)
		return reference.whereEqualTo("email", querys["email"])
			.whereEqualTo("password", querys["password"]).get()
	}


	fun saveFile(file: ByteArray, document: String = UUID.randomUUID().toString()): UploadTask {
		val fileReference = storage.child("comprovantes/$document")
		return fileReference.putBytes(file)
	}

	fun idNotIn(ids: List<String>): Task<List<DocumentSnapshot>> {
		if (ids.size <= 10) {
			return db.collection(collection.path).whereNotIn("chargeName", ids).get()
				.continueWith { task -> task.result?.documents ?: emptyList() }
		} else {
			val tasks = mutableListOf<Task<QuerySnapshot>>()
			ids.chunked(10).forEach { chunk ->
				tasks.add(db.collection(collection.path).whereNotIn("id", chunk).get())
			}

			return Tasks.whenAllSuccess<QuerySnapshot>(tasks)
				.continueWith { task -> task.result?.flatMap { it.documents } ?: emptyList() }
		}
	}

}