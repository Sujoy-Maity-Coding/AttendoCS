package com.sujoy.pbcadmin.domain.UseCase

import com.sujoy.pbcadmin.common.ResultState
import com.sujoy.pbcadmin.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddSemesterWebUrlUseCase @Inject constructor(private val repo: Repo) {
    fun addSemesterWebUrl(year: String, department: String, semester: String, webUrl: String) : Flow<ResultState<String>> = repo.addSemesterWebUrl(year, department, semester, webUrl)
}