export const validator = (name, value) => {
    const rules = {
        name: {
            required: { check: v => v.trim() !== "", message: "El nombre es obligatorio" },
            minLength: { check: v => v.length >= 3, message: "El nombre debe tener al menos 3 digitos" }
        },
        username: {
            required: { check: v => v.trim() !== "", message: "El username es obligatorio" },
            minLength: { check: v => v.length >= 3, message: "El username debe tener al menos 3 digitos" }
        },
        email: {
            required: { check: v => v.trim() !== "", message: "El correo electrónico es obligatorio" },
            pattern: { check: v => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v), message: "El correo electrónico no es válido" }
        }
    }
    if(!rules[name]) return {success: true, message: ""}
    for(const rule of Object.values(rules[name])){
        if(!rule.check(value)){
            return {success: false, message: rule.message}
        }
    }
    return { success: true, message: "" }
}
