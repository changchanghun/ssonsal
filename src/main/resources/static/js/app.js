function showLoading() {
  $("#loading-overlay").fadeIn(200);
}

function hideLoading() {
  $("#loading-overlay").fadeOut(200);
}

$(document).ready(function () {
  hideLoading();
});