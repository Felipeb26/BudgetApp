package com.batsworks.budget.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CustomRepository<T>(
	private val collection: FirebaseCollection<T>,
	private val type: Class<T>,
	private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
	private val storage: StorageReference = FirebaseStorage.getInstance().reference,
) {

	fun save(data: T): Task<DocumentReference> {
		return db.collection(collection.path).add(data as Any)
	}

	suspend fun findAll(): MutableList<DocumentSnapshot> {
		return db.collection(collection.path).get().await().documents
	}

	suspend fun findAllNotSync(): MutableList<DocumentSnapshot> {
		return db.collection(collection.path).get().await().documents
	}

	suspend fun findByDocument(document: String): T? {
		return db.collection(collection.path).document(document).get().await().toObject(type)
	}

	fun delete(document: String): Task<Void> {
		return db.collection(collection.path).document(document).delete()
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

	fun idNotIn(ids: List<String>): Task<List<DocumentSnapshot>> {
		if (ids.size <= 10) {
			return db.collection(collection.path).whereNotIn(FieldPath.documentId(), ids)
				.whereEqualTo("sync", false).get()
				.continueWith { task -> task.result?.documents ?: emptyList() }
		} else {
			val tasks = mutableListOf<Task<QuerySnapshot>>()
			ids.chunked(10).forEach { chunk ->
				tasks.add(
					db.collection(collection.path).whereNotIn(FieldPath.documentId(), chunk)
						.whereEqualTo("sync", false).get()
				)
			}

			return Tasks.whenAllSuccess<QuerySnapshot>(tasks)
				.continueWith { task -> task.result?.flatMap { it.documents } ?: emptyList() }
		}
	}

	fun saveFile(file: ByteArray, document: String = UUID.randomUUID().toString()): UploadTask {
		val fileReference = storage.child("comprovantes/$document")
		return fileReference.putBytes(file)
	}

	suspend fun retrieveFile(document: String): ByteArray {
		val fileReference = storage.child("comprovantes/$document")
		return fileReference.getBytes(Long.MAX_VALUE).await()
	}

	fun deleteFile(path: String): Boolean {
		return storage.child(path).delete().isSuccessful
	}
}
