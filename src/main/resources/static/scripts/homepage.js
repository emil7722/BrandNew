const cards = document.querySelectorAll(".news-card");
const buttons = document.querySelectorAll(".filter-btn");

buttons.forEach(btn => {
  btn.addEventListener("click", () => {
    const category = btn.dataset.category;
    cards.forEach(card => {
      const text = card.querySelector(".news-source").textContent;
      card.style.display = (category === "all" || text.includes(category)) ? "block" : "none";
    });
  });
});