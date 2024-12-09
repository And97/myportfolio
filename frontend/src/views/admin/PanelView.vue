<script setup lang="ts">
import WorkDetail from '@/components/admin/WorkDetail.vue'
import ManageWorks from '@/components/admin/ManageWorks.vue'
import UploadImages from '@/components/admin/UploadImages.vue'
import VisibleWorks from '@/components/admin/VisibleWorks.vue'
</script>

<template>
  <ul class="nav nav-tabs" id="myTab" role="tablist">
    <li class="nav-item" role="presentation">
      <button
        class="nav-link active"
        id="new-work-tab"
        data-bs-toggle="tab"
        data-bs-target="#new-work"
        type="button"
        role="tab"
        aria-controls="new-work"
        aria-selected="true"
      >
        Nuovo lavoro
      </button>
    </li>
    <li class="nav-item" role="presentation">
      <button
        class="nav-link"
        id="add-images-tab"
        data-bs-toggle="tab"
        data-bs-target="#add-images"
        type="button"
        role="tab"
        aria-controls="add-images"
        aria-selected="false"
      >
        Aggiungi immagini
      </button>
    </li>
    <li class="nav-item" role="presentation">
      <button
        class="nav-link"
        id="manage-work-tab"
        data-bs-toggle="tab"
        data-bs-target="#manage-work"
        type="button"
        role="tab"
        aria-controls="manage-work"
        aria-selected="false"
      >
        Gestisci lavori
      </button>
    </li>
    <li class="nav-item" role="presentation">
      <button
        class="nav-link"
        id="visible-works-tab"
        data-bs-toggle="tab"
        data-bs-target="#visible-works"
        type="button"
        role="tab"
        aria-controls="visible-works"
        aria-selected="false"
      >
        Gestisci visibilit&agrave; lavori
      </button>
    </li>
  </ul>
  <div class="tab-content">
    <div class="tab-pane active" id="new-work" role="tabpanel" aria-labelledby="new-work-tab">
      <WorkDetail />
    </div>
    <div class="tab-pane" id="add-images" role="tabpanel" aria-labelledby="add-images-tab">
      <UploadImages />
    </div>
    <div class="tab-pane" id="manage-work" role="tabpanel" aria-labelledby="manage-work-tab">
      <ManageWorks />
    </div>
    <div class="tab-pane" id="visible-works" role="tabpanel" aria-labelledby="visible-works-tab">
      <VisibleWorks />
    </div>
  </div>
</template>
