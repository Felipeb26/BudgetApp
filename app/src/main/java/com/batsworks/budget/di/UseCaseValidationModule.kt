package com.batsworks.budget.di

import com.batsworks.budget.use_cases.Validator
import com.batsworks.budget.use_cases.amount.ValidateChargeName
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseValidationModule {

	@Binds
	abstract fun validate(
		validateChargeName: ValidateChargeName
	): Validator<Any>
}