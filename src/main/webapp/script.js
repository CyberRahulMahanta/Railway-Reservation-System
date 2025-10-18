// Railway Reservation System JavaScript
document.addEventListener("DOMContentLoaded", () => {
  // Mobile menu toggle
  const menuToggle = document.querySelector(".menu-toggle")
  const nav = document.querySelector("nav")

  if (menuToggle) {
    menuToggle.addEventListener("click", () => {
      nav.classList.toggle("open")

      // Animate hamburger to X
      const spans = menuToggle.querySelectorAll("span")
      spans.forEach((span) => span.classList.toggle("active"))
    })
  }

  // Add animation classes to elements when they come into view
  const animateOnScroll = () => {
    const elements = document.querySelectorAll(".feature-card, .ticket-card, .form-container")

    elements.forEach((element) => {
      const elementPosition = element.getBoundingClientRect().top
      const screenPosition = window.innerHeight / 1.3

      if (elementPosition < screenPosition) {
        element.classList.add("slide-up")
      }
    })
  }

  // Run on load
  animateOnScroll()

  // Run on scroll
  window.addEventListener("scroll", animateOnScroll)

  // Ticket cancellation confirmation
  const cancelButtons = document.querySelectorAll('.btn-danger[onclick^="confirmCancel"]')

  cancelButtons.forEach((button) => {
    button.addEventListener("click", function (e) {
      if (
        !confirm("Are you sure you want to cancel this ticket? A refund will be processed according to the cancellation policy.")
      ) {
        e.preventDefault()
      }
    })
  })

  // Form validation
  const forms = document.querySelectorAll("form")

  forms.forEach((form) => {
    form.addEventListener("submit", (e) => {
      const requiredFields = form.querySelectorAll("[required]")
      let isValid = true

      requiredFields.forEach((field) => {
        if (!field.value.trim()) {
          isValid = false
          field.classList.add("error")

          // Create error message if it doesn't exist
          let errorMessage = field.nextElementSibling
          if (!errorMessage || !errorMessage.classList.contains("field-error")) {
            errorMessage = document.createElement("div")
            errorMessage.classList.add("field-error")
            errorMessage.textContent = "This field is required"
            field.parentNode.insertBefore(errorMessage, field.nextSibling)
          }
        } else {
          field.classList.remove("error")

          // Remove error message if it exists
          const errorMessage = field.nextElementSibling
          if (errorMessage && errorMessage.classList.contains("field-error")) {
            errorMessage.remove()
          }
        }
      })

      if (!isValid) {
        e.preventDefault()
      }
    })
  })

  // Date validation - prevent selecting past dates
  const dateInputs = document.querySelectorAll('input[type="date"]')

  dateInputs.forEach((input) => {
    const today = new Date().toISOString().split("T")[0]
    input.setAttribute("min", today)
  })

  // Add passenger form dynamically
  const addPassengerBtn = document.querySelector(".add-passenger-btn")

  if (addPassengerBtn) {
    addPassengerBtn.addEventListener("click", () => {
      const passengerForms = document.querySelector(".passenger-forms")
      const passengerFormTemplate = document.querySelector(".passenger-form").cloneNode(true)

      // Update form index
      const formIndex = document.querySelectorAll(".passenger-form").length

      // Update form IDs and names
      const inputs = passengerFormTemplate.querySelectorAll("input, select")
      inputs.forEach((input) => {
        const name = input.getAttribute("name")
        if (name) {
          input.setAttribute("name", name.replace(/\[\d+\]/, `[${formIndex}]`))
        }

        // Clear values
        input.value = ""
      })

      // Add remove button if it's not the first form
      if (formIndex > 0) {
        const removeBtn = document.createElement("button")
        removeBtn.type = "button"
        removeBtn.className = "btn btn-danger btn-small remove-passenger-btn"
        removeBtn.textContent = "Remove"
        removeBtn.addEventListener("click", () => {
          passengerFormTemplate.remove()
          updateTotalFare()
        })

        passengerFormTemplate.appendChild(removeBtn)
      }

      passengerForms.appendChild(passengerFormTemplate)
      updateTotalFare()
    })
  }

  // Update total fare based on number of passengers
  function updateTotalFare() {
    const baseFare = document.querySelector("[data-base-fare]")
    const totalFareElement = document.querySelector("[data-total-fare]")

    if (baseFare && totalFareElement) {
      const farePerPassenger = Number.parseFloat(baseFare.getAttribute("data-base-fare"))
      const passengerCount = document.querySelectorAll(".passenger-form").length
      const totalFare = farePerPassenger * passengerCount

      totalFareElement.textContent = totalFare.toFixed(2)

      // Update hidden input for form submission
      const totalFareInput = document.querySelector('input[name="totalFare"]')
      if (totalFareInput) {
        totalFareInput.value = totalFare.toFixed(2)
      }
    }
  }

  // Initialize tooltips
  const tooltips = document.querySelectorAll("[data-tooltip]")

  tooltips.forEach((tooltip) => {
    tooltip.addEventListener("mouseenter", function () {
      const tooltipText = this.getAttribute("data-tooltip")
      const tooltipElement = document.createElement("div")
      tooltipElement.className = "tooltip"
      tooltipElement.textContent = tooltipText

      document.body.appendChild(tooltipElement)

      const rect = this.getBoundingClientRect()
      tooltipElement.style.top = `${rect.top - tooltipElement.offsetHeight - 10}px`
      tooltipElement.style.left = `${rect.left + (rect.width / 2) - tooltipElement.offsetWidth / 2}px`
      tooltipElement.style.opacity = "1"
    })

    tooltip.addEventListener("mouseleave", () => {
      const tooltipElement = document.querySelector(".tooltip")
      if (tooltipElement) {
        tooltipElement.remove()
      }
    })
  })
})

