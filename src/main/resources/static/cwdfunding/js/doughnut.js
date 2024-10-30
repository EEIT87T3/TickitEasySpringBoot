$(document).ready(function () {
  const ctx = document.getElementById("myDoughnutChart").getContext("2d");
  const targetAmount = document
    .getElementById("targetAmount")
    .getAttribute("data-targetamount");
  const currentAmount = document
    .getElementById("currentAmount")
    .getAttribute("data-currentamount");
  const nowProgress = (
    (parseInt(currentAmount) / parseInt(targetAmount)) *
    100
  ).toFixed(0);
  let leftProgress;
  if (100 - nowProgress >= 0) {
    leftProgress = 100 - nowProgress;
  } else {
    leftProgress = 0;
  }

  const myDoughnutChart = new Chart(ctx, {
    type: "doughnut",
    data: {
      datasets: [
        {
          label: "我的數據",
          data: [nowProgress, leftProgress],
          backgroundColor: ["#5f46e8", "#e0e0e0"],
          borderColor: ["#5f46e8", "#e0e0e0"],
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      cutout: "95%", // 調整圓心的大小以使 doughnut 更細
      animation: {
        animateScale: true,
        animateRotate: true,
      },
      plugins: {
        // 在中間顯示文字
        tooltip: {
          enabled: false, // 禁用 tooltip
        },
        legend: {
          display: false, // 禁用圖例
        },
        // 自定義插件
        datalabels: {
          display: true,
          align: "center",
          anchor: "center",
          font: {
            size: "14",
            weight: "bold",
          },
          formatter: function () {
            return `${nowProgress}%`; // 在中間顯示的文本
          },
        },
      },
    },
    plugins: [
      {
        beforeDraw: function (chart) {
          const ctx = chart.ctx;
          const x = chart.width / 2;
          const y = chart.height / 2;
          ctx.restore();
          ctx.font = "bold 14px Arial";
          ctx.fillStyle = "#000"; // 文字顏色
          ctx.textAlign = "center";
          ctx.textBaseline = "middle";
          ctx.fillText(`${nowProgress}%`, x, y); // 中間的文字
          ctx.save();
        },
      },
    ],
  });
});
