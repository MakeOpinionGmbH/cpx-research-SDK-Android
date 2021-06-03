package com.makeopinion.cpxresearchlib.misc

import com.makeopinion.cpxresearchlib.models.SurveyModel

class CPXJsonValidator {
    companion object {
        fun isValidSurveyModel(model: SurveyModel): Boolean {
            return model.status != null && model.status == "success"
        }
    }
}