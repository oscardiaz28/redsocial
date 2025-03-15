import { useState } from "react"
import { names } from "../libs/mapper"
import { validator } from "../libs/validator"

const useForm = ( initialObject = {}, validate, onSubmit ) => {

    const [formData, setFormData] = useState(initialObject)
    const [errors, setErrors] = useState({})
    const [isSubmit, setIsSubmit] = useState(false)

    const handleSubmit = e => {
        setIsSubmit(true)
        e.preventDefault()
        const validationErrors = validate(formData)
        setErrors(validationErrors)

        if(Object.keys(validationErrors).length === 0){
            onSubmit(formData)
            setIsSubmit(false)
        }
    }

    const handleChange = (e) => {
        const input = e.target
        const name = input.name
        const value = input.value
        setFormData( {
            ...formData,
            [name]: value
        })
        setErrors( (prev) => {
            const isValid = validator(name, value)
            return {
                ...prev,
                [name]: isValid.success ? "" : isValid.message
            }
        })
    }

    return {
        formData,
        handleChange,
        handleSubmit,
        errors,
        setFormData,
        isSubmit
    }

}

export default useForm